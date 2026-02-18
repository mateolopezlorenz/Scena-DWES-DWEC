
//Datos necesarios para crear un nuevo evento.

export interface CreateEventRequest {
  name: string;
  description: string;
  category: string;
  startDate: string;
  endDate: string;
  latitude: number;
  longitude: number;
  address: string;
}