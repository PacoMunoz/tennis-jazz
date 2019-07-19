import { IMatchTennis } from 'app/shared/model/match-tennis.model';
import { ITournamentGroupTennis } from 'app/shared/model/tournament-group-tennis.model';

export interface IPlayerTennis {
  id?: number;
  name?: string;
  surname?: string;
  email?: string;
  phone?: string;
  other?: string;
  visitorMatches?: IMatchTennis[];
  localMatches?: IMatchTennis[];
  groups?: ITournamentGroupTennis[];
}

export class PlayerTennis implements IPlayerTennis {
  constructor(
    public id?: number,
    public name?: string,
    public surname?: string,
    public email?: string,
    public phone?: string,
    public other?: string,
    public visitorMatches?: IMatchTennis[],
    public localMatches?: IMatchTennis[],
    public groups?: ITournamentGroupTennis[]
  ) {}
}
