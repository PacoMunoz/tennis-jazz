import { ITournamentTennis } from 'app/shared/model/tournament-tennis.model';
import { IRoundTennis } from 'app/shared/model/round-tennis.model';
import { IPlayerTennis } from 'app/shared/model/player-tennis.model';

export interface ITournamentGroupTennis {
  id?: number;
  name?: string;
  tournament?: ITournamentTennis;
  rounds?: IRoundTennis[];
  players?: IPlayerTennis[];
}

export class TournamentGroupTennis implements ITournamentGroupTennis {
  constructor(
    public id?: number,
    public name?: string,
    public tournament?: ITournamentTennis,
    public rounds?: IRoundTennis[],
    public players?: IPlayerTennis[]
  ) {}
}
