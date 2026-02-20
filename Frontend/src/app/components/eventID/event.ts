import { Component, OnInit, AfterViewChecked } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { EventService } from '../../services/eventService';
import { UserEventService } from '../../services/userEventService';
import { LocalService } from '../../services/localService';
import { Events } from '../../models/eventModel';
import { Local } from '../../models/localModel';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MapView } from '../map-view/map-view';
import * as L from 'leaflet';

@Component({
  selector: 'app-event',
  imports: [CommonModule, FormsModule, MapView],
  templateUrl: './event.html',
  styleUrls: ['./event.scss'],
})
export class Event implements OnInit, AfterViewChecked {
  event: Events | null = null;
  eventId: number | null = null;
  usuarioActivo: number | null = null;
  usuarioEditando: boolean = false;
  usuarioEliminando: boolean = false;
  conformacionEliminar: string = '';
  isLoggedIn: boolean = false;
  liked: boolean = false;
  showEditMapSelector: boolean = false;
  locals: Local[] = [];
  private map: any = null;
  private mapInitialized: boolean = false;

  editedEvent: {
    name: string;
    description: string;
    category: string;
    startDate: string;
    endDate: string;
    latitude: number | null;
    longitude: number | null;
    address: string;
    localId: number | null;
  } = {
    name: '',
    description: '',
    category: '',
    startDate: '',
    endDate: '',
    latitude: null,
    longitude: null,
    address: '',
    localId: null
  };

  constructor(private route: ActivatedRoute, private eventService: EventService, private userEventService: UserEventService, private localService: LocalService, private router: Router) {}

  ngOnInit(): void {
    this.isLoggedIn = !!localStorage.getItem('token');
    this.usuarioActivado();

    if (this.isLoggedIn) {
      this.localService.getLocals().subscribe({
        next: (data: Local[]) => this.locals = data,
        error: (err) => console.error('Error al cargar locales', err)
      });
    }

    this.route.params.subscribe(params => {
      this.eventId = params['id'];
      if (this.eventId) {
        this.loadEvent();
      }
    });
  }

  //Método para obtener el ID del usuario autenticado.
  usuarioActivado() {
    this.usuarioActivo = +localStorage.getItem('id')!;
  }

  //Método que carga los datos del evento a través del servicio.
  loadEvent(): void {
    if (this.eventId) {
      this.eventService.getEventById(this.eventId).subscribe({
        next: (data: Events) => {
          this.event = data;
          this.datosDeInicio();
          if (this.isLoggedIn) {
            this.loadLikeStatus();
          }
        },
        error: (err) => console.error('Error al cargar el evento', err)
      });
    }
  }

  loadLikeStatus(): void {
    this.userEventService.getLikedByUser().subscribe({
      next: (likedEvents: Events[]) => {
        this.liked = likedEvents.some(e => e.id === this.event?.id);
      },
      error: () => this.liked = false
    });
  }

  toggleLike(): void {
    if (!this.event) return;
    if (this.liked) {
      this.userEventService.removeLike(this.event.id).subscribe({
        next: () => this.liked = false,
        error: (err) => console.error('Error al quitar like', err)
      });
    } else {
      this.userEventService.addLike(this.event.id).subscribe({
        next: () => this.liked = true,
        error: (err) => console.error('Error al dar like', err)
      });
    }
  }

  //Método para verificar si el usuario es el creador del evento.
  esCreador(): boolean {
    return this.usuarioActivo !== null && this.event?.user?.id === this.usuarioActivo;
  }

  //Método para inicializar el formulario de edición.
  datosDeInicio() {
    if (this.event) {
      this.editedEvent = {
        name: this.event.name,
        description: this.event.description,
        category: this.event.category,
        startDate: this.event.startDate,
        endDate: this.event.endDate,
        latitude: this.event.latitude,
        longitude: this.event.longitude,
        address: this.event.address,
        localId: this.event.local ? this.event.local.id : -1
      };
    }
  }

  onEditLocalSelected() {
    if (this.editedEvent.localId) {
      const local = this.locals.find(l => l.id === +this.editedEvent.localId!);
      if (local) {
        this.editedEvent.latitude = local.latitude;
        this.editedEvent.longitude = local.longitude;
        this.editedEvent.address = local.ubication;
      }
    }
  }

  //Método para capturar coordenadas del mapa en edición
  onEditCoordinatesSelected(coordinates: { latitude: number, longitude: number, address: string }) {
    this.editedEvent.latitude = coordinates.latitude;
    this.editedEvent.longitude = coordinates.longitude;
    this.editedEvent.address = coordinates.address;
    this.showEditMapSelector = false;
  }

  //Método para mostrar el formulario de edición.
  showEditForm() {
    this.usuarioEditando = true;
  }

  //Método para cancelar la edición.
  cancelEdit() {
    this.usuarioEditando = false;
    this.datosDeInicio();
  }

  //Método para guardar los cambios.
  saveChanges() {
    if (!this.event?.id) return;

    this.eventService.updateEvent(this.event.id, this.editedEvent).subscribe({
      next: () => {
        alert('Evento actualizado correctamente');
        this.usuarioEditando = false;
        this.loadEvent();
      },
      error: (err) => {
        const errorMsg = err.error?.message || err.error || err.message || 'Error desconocido';
        alert('Error al actualizar el evento: ' + errorMsg);
        console.error('Error al actualizar el evento:', err);
      }
    });
  }

  //Método para mostrar la confirmación de eliminación.
  showDeleteConfirmation() {
    this.usuarioEliminando = true;
    this.conformacionEliminar = '';
  }

  //Método para cancelar la eliminación.
  cancelDelete() {
    this.usuarioEliminando = false;
    this.conformacionEliminar = '';
  }

  //Método para confirmar y eliminar el evento.
  confirmDelete() {
    if (!this.event) return;

    if (this.conformacionEliminar.trim() !== this.event.name) {
      alert('El nombre del evento no coincide. No se ha eliminado.');
      return;
    }

    if (!this.event.id) return;

    this.eventService.deleteEvent(this.event.id).subscribe({
      next: () => {
        alert('Evento eliminado correctamente');
        this.router.navigate(['/event-list']);
      },
      error: (err) => {
        const errorMsg = err.error?.message || err.error || err.message || 'Error desconocido';
        alert('Error al eliminar el evento: ' + errorMsg);
        console.error('Error al eliminar el evento:', err);
      }
    });
  }

  //Inicializar mapa cuando el DOM esté listo
  ngAfterViewChecked(): void {
    if (!this.mapInitialized && this.event?.latitude && this.event?.longitude && !this.usuarioEditando && !this.usuarioEliminando) {
      const mapEl = document.getElementById('event-map');
      if (mapEl) {
        this.initMap();
      }
    }
  }

  private initMap(): void {
    if (!this.event) return;
    this.mapInitialized = true;

    const lat = this.event.latitude;
    const lng = this.event.longitude;

    // Fix iconos Leaflet
    const defaultIcon = L.icon({
      iconUrl: 'https://unpkg.com/leaflet@1.7.1/dist/images/marker-icon.png',
      iconRetinaUrl: 'https://unpkg.com/leaflet@1.7.1/dist/images/marker-icon-2x.png',
      shadowUrl: 'https://unpkg.com/leaflet@1.7.1/dist/images/marker-shadow.png',
      iconSize: [25, 41],
      iconAnchor: [12, 41],
      popupAnchor: [1, -34],
      shadowSize: [41, 41]
    });
    L.Marker.prototype.options.icon = defaultIcon;

    this.map = L.map('event-map', {
      center: [lat, lng],
      zoom: 15
    });

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '© OpenStreetMap contributors'
    }).addTo(this.map);

    const popupContent = this.event.local
      ? `<b>${this.event.local.name}</b><br>${this.event.local.ubication}`
      : `<b>${this.event.name}</b><br>${this.event.address}`;

    L.marker([lat, lng])
      .addTo(this.map)
      .bindPopup(popupContent)
      .openPopup();
  }
}
