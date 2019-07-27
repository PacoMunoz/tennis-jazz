import { ITournamentGroupTennis } from 'app/shared/model/tournament-group-tennis.model';
import { IPlayerTennis } from 'app/shared/model/player-tennis.model';

export interface IRankingTennis {
  id?: number;
  points?: number;
  gamesWin?: number;
  gamesLoss?: number;
  setsWin?: number;
  setsLost?: number;
  matchesPlayed?: number;
  matchesWined?: number;
  matchesLoss?: number;
  tournamentGroup?: ITournamentGroupTennis;
  player?: IPlayerTennis;
}

export class RankingTennis implements IRankingTennis {
  constructor(
    public id?: number,
    public points?: number,
    public gamesWin?: number,
    public gamesLoss?: number,
    public setsWin?: number,
    public setsLost?: number,
    public matchesPlayed?: number,
    public matchesWined?: number,
    public matchesLoss?: number,
    public tournamentGroup?: ITournamentGroupTennis,
    public player?: IPlayerTennis
  ) {}
}
