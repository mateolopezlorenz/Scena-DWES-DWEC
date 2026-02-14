export interface Events {
  id: number;
  name: string;
  description: string;
  category: string;
  startDate: string;
  endDate: string;
  capacity: number;
  rooms: number;
  userId: number;
  localId: number;
}

export interface CreateEventRequest {
  name: string;
  description: string;
  startDate: string;
  endDate: string;
  localId: number;
}

export interface UpdateEventRequest {
  name?: string;
  description?: string;
  startDate?: string;
  endDate?: string;
  localId: number;
  latitude?: number;
  longitude?: number;
}

export interface deleteEventRequest {
  id: number;
}