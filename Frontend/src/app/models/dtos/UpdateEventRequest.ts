import { Local } from '../localModel';

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