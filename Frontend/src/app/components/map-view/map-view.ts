import { Component, AfterViewInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import * as L from 'leaflet';

@Component({
  selector: 'app-map-view',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './map-view.html',
  styleUrls: ['./map-view.scss']
})
export class MapView implements AfterViewInit {
  private map: any;

  // DATOS MOCK (Simulados según el PDF para ver algo ya)
  private events = [
    {
      id: 1,
      name: 'Concierto de Verano',
      category: 'Música',
      start_date: '2026-02-15',
      local: { 
        name: 'Palma Arena',
        latitude: 39.5896, 
        longitude: 2.6502 
      }
    },
    {
      id: 2,
      name: 'Festival de Teatro',
      category: 'Cultura',
      start_date: '2026-02-20',
      local: {
        name: 'Teatre d\'Alcúdia',
        latitude: 39.8530,
        longitude: 3.1237
      }
    },
    {
      id: 3,
      name: 'Torneo de Padel',
      category: 'Esport',
      start_date: '2026-02-22',
      local: {
        name: 'Rafa Nadal Academy',
        latitude: 39.5539,
        longitude: 3.2036
      }
    }
  ];

  constructor() {}

  ngAfterViewInit(): void {
    this.initMap();
  }

  private initMap(): void {
    // 1. Configuración del mapa (Mallorca)
    this.map = L.map('map').setView([39.695, 3.018], 10);

    // 2. Capa de tiles (OpenStreetMap)
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 18,
      attribution: '© OpenStreetMap contributors'
    }).addTo(this.map);

    // 3. Arreglar el bug de iconos de Leaflet
    this.fixLeafletIcons();

    // 4. Añadir marcadores
    this.addMarkers();
  }

  private addMarkers(): void {
    this.events.forEach(event => {
      if (event.local && event.local.latitude && event.local.longitude) {
        
        const marker = L.marker([event.local.latitude, event.local.longitude]);

        // HTML del Popup
        const popupContent = `
          <div style="text-align: center;">
            <h3 style="margin: 0 0 5px 0; color: #333;">${event.name}</h3>
            <span style="background: #eee; padding: 2px 6px; border-radius: 4px; font-size: 12px;">
              ${event.category}
            </span>
            <p style="margin: 8px 0;">📍 ${event.local.name}</p>
            <p>📅 ${event.start_date}</p>
          </div>
        `;

        marker.bindPopup(popupContent);
        marker.addTo(this.map);
      }
    });
  }

  // Hack para los iconos
  private fixLeafletIcons(): void {
    const defaultIcon = L.icon({
      iconUrl: 'https://unpkg.com/leaflet@1.7.1/dist/images/marker-icon.png',
      iconRetinaUrl: 'https://unpkg.com/leaflet@1.7.1/dist/images/marker-icon-2x.png',
      shadowUrl: 'https://unpkg.com/leaflet@1.7.1/dist/images/marker-shadow.png',
      iconSize: [25, 41],
      iconAnchor: [12, 41],
      popupAnchor: [1, -34],
      tooltipAnchor: [16, -28],
      shadowSize: [41, 41]
    });
    L.Marker.prototype.options.icon = defaultIcon;
  }
}