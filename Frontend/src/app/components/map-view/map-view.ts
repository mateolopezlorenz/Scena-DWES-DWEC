import { Component, AfterViewInit, Output, EventEmitter } from '@angular/core';
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
  private selectedMarker: any = null;
  
  @Output() coordinatesSelected = new EventEmitter<{ latitude: number, longitude: number, address: string }>();

  constructor() {}

  ngAfterViewInit(): void {
    this.initMap();
  }

  private initMap(): void {
    const southWest = L.latLng(39.20, 2.25);
    const northEast = L.latLng(40.05, 3.50);
    const mallorcaBounds = L.latLngBounds(southWest, northEast);

    this.map = L.map('map', {
      center: [39.695, 3.018],
      zoom: 10,
      minZoom: 9.8,
      maxZoom: 16,
      maxBounds: mallorcaBounds,
      maxBoundsViscosity: 1.0
    });

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 18,
      attribution: '© OpenStreetMap contributors'
    }).addTo(this.map);

    this.fixLeafletIcons();
    this.addMapClickListener();
  }

  private addMapClickListener(): void {
    this.map.on('click', (e: any) => {
      const lat = e.latlng.lat;
      const lng = e.latlng.lng;
      this.selectCoordinates(lat, lng);
    });
  }

  private selectCoordinates(lat: number, lng: number): void {
    // Remover marcador anterior si existe
    if (this.selectedMarker) {
      this.map.removeLayer(this.selectedMarker);
    }

    // Crear nuevo marcador de selección
    const selectedIcon = L.icon({
      iconUrl: 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-blue.png',
      iconSize: [25, 41],
      iconAnchor: [12, 41],
      popupAnchor: [1, -34],
      shadowSize: [41, 41]
    });

    this.selectedMarker = L.marker([lat, lng], { icon: selectedIcon })
      .bindPopup(`<b>Coordenadas seleccionadas</b><br>Lat: ${lat.toFixed(4)}<br>Lng: ${lng.toFixed(4)}`)
      .addTo(this.map)
      .openPopup();

    // Emitir coordenadas al componente padre
    this.coordinatesSelected.emit({
      latitude: lat,
      longitude: lng,
      address: `${lat.toFixed(4)}, ${lng.toFixed(4)}`
    });
  }

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