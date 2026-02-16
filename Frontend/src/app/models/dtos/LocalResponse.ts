import { UserResponse } from './UserResponse';
import { EventBasicInfo } from './EventBasicInfo';

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