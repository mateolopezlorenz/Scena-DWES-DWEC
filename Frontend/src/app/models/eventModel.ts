import { User } from './userModel';
import { Local } from './localModel';

export interface Events {
  id: number;
  name: string;
  description: string;
  category: string;
  startDate: string;
  endDate: string;
  latitude: number;
  longitude: number;
  address: string;
  user: User;
  local: Local;
}

