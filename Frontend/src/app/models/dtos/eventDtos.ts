import { Local } from '../localModel';

// Respuesta con información básica de un usuario.
export interface UserResponse {
  id: number;
  username: string;
  email: string;
}

//Respuesta detallada de un evento, incluyendo información del usuario creador y el local.
export interface EventResponse {
  id: number;
  name: string;
  description: string;
  category: string;
  startDate: string;
  endDate: string;
  capacity: number;
  rooms: number;
  createdAt: string;
  user: UserResponse;
  local: Local;
}

//Datos necesarios para crear un nuevo evento.
export interface CreateEventRequest {
  name: string;
  description: string;
  category: string;
  startDate: string;
  endDate: string;
  capacity: number;
  rooms: number;
  localId: number;
}

//Datos necesarios para actualizar un evento.
export interface UpdateEventRequest {
  name: string;
  description: string;
  category: string;
  startDate: string;
  endDate: string;
  capacity: number;
  rooms: number;
}

//Información resumida de un evento.
export interface EventBasicInfo {
  id: number;
  name: string;
  category: string;
  startDate: string;
  endDate: string;
  capacity: number;
}
