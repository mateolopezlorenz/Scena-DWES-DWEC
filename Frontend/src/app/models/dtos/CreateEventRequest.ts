import { Local } from '../localModel';

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