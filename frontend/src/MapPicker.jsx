import { useState } from "react";
import { MapContainer, TileLayer, CircleMarker, Polyline, Popup, useMapEvents } from "react-leaflet";
import "leaflet/dist/leaflet.css";

function MapClick({ onSelect }) {
  useMapEvents({ click(event) { onSelect([event.latlng.lat, event.latlng.lng]); } });
  return null;
}

export default function MapPicker({ pickupPosition, dropPosition, onPickupSelect, onDropSelect }) {
  const [selecting, setSelecting] = useState("pickup");
  function handleMapClick(position) {
    if (selecting === "pickup") onPickupSelect(position);
    else onDropSelect(position);
  }
  return (
    <div className="map-picker">
      <div className="map-picker-heading"><h3>Select locations on map</h3><p>Choose pickup or drop, then click the map.</p></div>
      <div className="map-actions">
        <button type="button" className={selecting === "pickup" ? "map-select-btn active" : "map-select-btn"} onClick={() => setSelecting("pickup")}>Pickup location</button>
        <button type="button" className={selecting === "drop" ? "map-select-btn active" : "map-select-btn"} onClick={() => setSelecting("drop")}>Drop location</button>
      </div>
      <p className="map-hint">Currently selecting: <strong>{selecting}</strong></p>
      <MapContainer center={pickupPosition} zoom={12} scrollWheelZoom style={{ height: "300px", width: "100%", borderRadius: "12px" }}>
        <TileLayer attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors' url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />
        <MapClick onSelect={handleMapClick} />
        <CircleMarker center={pickupPosition} radius={8} pathOptions={{ color: "#15803d", fillColor: "#22c55e", fillOpacity: 1 }}><Popup>Pickup location</Popup></CircleMarker>
        <CircleMarker center={dropPosition} radius={8} pathOptions={{ color: "#b91c1c", fillColor: "#ef4444", fillOpacity: 1 }}><Popup>Drop location</Popup></CircleMarker>
        <Polyline positions={[pickupPosition, dropPosition]} pathOptions={{ color: "#eab308", weight: 4, dashArray: "8 8" }} />
      </MapContainer>
      <div className="map-coordinates"><p><strong>Pickup:</strong> {pickupPosition.map(v => v.toFixed(5)).join(", ")}</p><p><strong>Drop:</strong> {dropPosition.map(v => v.toFixed(5)).join(", ")}</p></div>
    </div>
  );
}
