export interface Local {
  id: number;
  name: string;
  latitude: number;
  longitude: number;
  ubication: string;
  capacity: number;
  rooms: number;
}

export interface CreateLocalRequest {
  name: string;
  latitude: number;
  longitude: number;
  ubication: string;
  capacity: number;
  rooms: number;
}

export interface UpdateLocalRequest {
  name?: string;
  latitude?: number;
  longitude?: number;
  ubication?: string;
  capacity?: number;
  rooms?: number;
}

export interface DeleteLocalRequest {
  id: number;
}
