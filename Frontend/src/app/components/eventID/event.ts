import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { EventService } from '../../services/eventService';
import { Events } from '../../models/eventModel';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-event',
  imports: [RouterModule, CommonModule, FormsModule],
  templateUrl: './event.html',
  styleUrls: ['./event.scss'],
})
export class Event implements OnInit {
  event: Events | null = null;
  eventId: number | null = null;
  usuarioActivo: number | null = null;
  usuarioEditando: boolean = false;
  usuarioEliminando: boolean = false;
  conformacionEliminar: string = '';

  editedEvent: {
    name: string;
    description: string;
    category: string;
    startDate: string;
    endDate: string;
    capacity: number;
    rooms: number;
    localId: number | null;
  } = {
    name: '',
    description: '',
    category: '',
    startDate: '',
    endDate: '',
    capacity: 1,
    rooms: 1,
    localId: null
  };

  constructor(private route: ActivatedRoute, private eventService: EventService ) {}

  //Obtenemos el ID del evento y cargamos sus datos.
  ngOnInit(): void {
    this.usuarioActivado();

    //Método con el cual obtenemos el ID del evento y cargamos sus datos.
    this.route.params.subscribe(params => {

      //Obtenemos el ID del evento.
      this.eventId = params['id'];

      //Si el ID existe, cargamos los datos.
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
        },
        error: (err) => console.error('Error al cargar el evento', err)
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
        capacity: this.event.capacity,
        rooms: this.event.rooms,
        localId: this.event.local?.id ?? null
      };
    }
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
        window.location.href = '/events';
      },
      error: (err) => {
        const errorMsg = err.error?.message || err.error || err.message || 'Error desconocido';
        alert('Error al eliminar el evento: ' + errorMsg);
        console.error('Error al eliminar el evento:', err);
      }
    });
  }
}
