import { UserResponse } from './eventDtos';
import { EventBasicInfo } from './eventDtos';

//Respuesta detallada de un local, incluyendo el usuario propietario y sus eventos.
export interface LocalResponse {
  id: number;
  name: string;
  latitude: number;
  longitude: number;
  ubication: string;
  capacity: number;
  rooms: number;
  user: UserResponse;
  events: EventBasicInfo[];
}

//Datos necesarios para registrar un nuevo local.
export interface CreateLocalRequest {
  name: string;
  latitude: number;
  longitude: number;
  ubication: string;
  capacity: number;
  rooms: number;
}

//Datos necesarios para actualizar la información de un local.
export interface UpdateLocalRequest {
  name: string;
  latitude: number;
  longitude: number;
  ubication: string;
  capacity: number;
  rooms: number;
}
