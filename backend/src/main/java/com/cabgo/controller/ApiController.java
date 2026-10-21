package com.cabgo.controller;

import com.cabgo.model.*;
import com.cabgo.repository.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ApiController {
    private final UserRepository users;
    private final RideRepository rides;
    private final PaymentRepository payments;
    private final PasswordEncoder encoder;

    public ApiController(UserRepository users, RideRepository rides, PaymentRepository payments, PasswordEncoder encoder) {
        this.users=users; this.rides=rides; this.payments=payments; this.encoder=encoder;
    }

    @GetMapping("/health") public Map<String,Object> health(){return Map.of("status","ok","service","CabGo API");}

    @GetMapping("/cabs") public List<Map<String,Object>> cabs(){
        return List.of(
          Map.of("id","MINI","name","Mini","description","Affordable city rides","seats",4,"baseFare",80,"perKm",12,"eta","3 min","icon","🚕"),
          Map.of("id","SEDAN","name","Sedan","description","Comfortable everyday rides","seats",4,"baseFare",120,"perKm",16,"eta","4 min","icon","🚖"),
          Map.of("id","SUV","name","SUV","description","Extra space for groups","seats",6,"baseFare",180,"perKm",22,"eta","6 min","icon","🚙")
        );
    }

    public record AuthRequest(@NotBlank String name, @Email @NotBlank String email, @NotBlank String password, String role) {}
    public record LoginRequest(@Email @NotBlank String email, @NotBlank String password) {}
    public record UserView(Long id,String name,String email,String role) {}
    private UserView view(AppUser u){return new UserView(u.getId(),u.getName(),u.getEmail(),u.getRole());}

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@Valid @RequestBody AuthRequest req) {
        if(users.findByEmailIgnoreCase(req.email()).isPresent()) return ResponseEntity.status(409).body(Map.of("message","Email already registered"));
        String role = "DRIVER".equalsIgnoreCase(req.role()) ? "DRIVER" : "RIDER";
        AppUser u=users.save(new AppUser(req.name().trim(),req.email().trim().toLowerCase(),encoder.encode(req.password()),role));
        return ResponseEntity.ok(view(u));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        return users.findByEmailIgnoreCase(req.email()).filter(u->encoder.matches(req.password(),u.getPasswordHash()))
          .<ResponseEntity<?>>map(u->ResponseEntity.ok(view(u)))
          .orElseGet(()->ResponseEntity.status(401).body(Map.of("message","Invalid email or password")));
    }

    public record RideRequest(@NotBlank String riderName,@Email @NotBlank String riderEmail,
      @NotBlank String pickup,@NotBlank String dropLocation,@NotBlank String cabType,double distanceKm) {}

    @PostMapping("/rides")
    public ResponseEntity<?> book(@Valid @RequestBody RideRequest req) {
        if(req.distanceKm() <= 0 || req.distanceKm() > 500) return ResponseEntity.badRequest().body(Map.of("message","Distance must be between 0 and 500 km"));
        Map<String,Integer> base=Map.of("MINI",80,"SEDAN",120,"SUV",180);
        Map<String,Integer> perKm=Map.of("MINI",12,"SEDAN",16,"SUV",22);
        String type=req.cabType().toUpperCase();
        if(!base.containsKey(type)) return ResponseEntity.badRequest().body(Map.of("message","Unknown cab type"));
        Ride r=new Ride(); r.setRiderName(req.riderName());r.setRiderEmail(req.riderEmail().toLowerCase());
        r.setPickup(req.pickup());r.setDropLocation(req.dropLocation());r.setCabType(type);
        r.setDistanceKm(req.distanceKm());r.setFare(Math.round(base.get(type)+req.distanceKm()*perKm.get(type)));
        return ResponseEntity.ok(rides.save(r));
    }

    @GetMapping("/rides") public List<Ride> myRides(@RequestParam String email) {
        return rides.findByRiderEmailIgnoreCaseOrderByCreatedAtDesc(email);
    }
    @GetMapping("/rides/all") public List<Ride> allRides(){return rides.findAll().stream().sorted(Comparator.comparing(Ride::getCreatedAt).reversed()).collect(Collectors.toList());}

    @PatchMapping("/rides/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id,@RequestParam String status) {
        Optional<Ride> found=rides.findById(id);
        if(found.isEmpty()) return ResponseEntity.notFound().build();
        Ride r=found.get(); String next=status.toUpperCase();
        if(!Set.of("ACCEPTED","IN_PROGRESS","COMPLETED","CANCELLED").contains(next))
            return ResponseEntity.badRequest().body(Map.of("message","Invalid ride status"));
        r.setStatus(next);
        if(next.equals("ACCEPTED")) r.setDriverName("Rohit Kumar");
        return ResponseEntity.ok(rides.save(r));
    }

    public record PaymentRequest(Long rideId,String riderEmail,double amount,String method) {}
    @PostMapping("/payments")
    public ResponseEntity<?> pay(@RequestBody PaymentRequest req) {
        if(req.rideId()==null || req.riderEmail()==null || req.method()==null || req.amount()<=0)
            return ResponseEntity.badRequest().body(Map.of("message","Valid ride, email, method and amount are required"));
        if(rides.findById(req.rideId()).isEmpty()) return ResponseEntity.badRequest().body(Map.of("message","Ride not found"));
        Payment p=payments.save(new Payment(req.rideId(),req.riderEmail().toLowerCase(),req.amount(),req.method()));
        return ResponseEntity.ok(p);
    }
    @GetMapping("/payments") public List<Payment> myPayments(@RequestParam String email) {
        return payments.findByRiderEmailIgnoreCaseOrderByCreatedAtDesc(email);
    }
    @GetMapping("/admin/summary") public Map<String,Object> summary(){
        List<Ride> all=rides.findAll();
        double revenue=payments.findAll().stream().mapToDouble(Payment::getAmount).sum();
        return Map.of("totalUsers",users.count(),"totalDrivers",users.findAll().stream().filter(u->"DRIVER".equals(u.getRole())).count(),
          "totalRides",all.size(),"totalRevenue",Math.round(revenue*100.0)/100.0,
          "requestedRides",all.stream().filter(r->"REQUESTED".equals(r.getStatus())).count(),
          "completedRides",all.stream().filter(r->"COMPLETED".equals(r.getStatus())).count());
    }
}
