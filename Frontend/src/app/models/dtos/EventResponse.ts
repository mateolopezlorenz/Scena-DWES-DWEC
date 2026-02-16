import { Local } from '../localModel';
import { UserResponse } from './UserResponse';


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