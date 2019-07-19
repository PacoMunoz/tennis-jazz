import { IRoundTennis } from 'app/shared/model/round-tennis.model';
import { IPlayerTennis } from 'app/shared/model/player-tennis.model';

export interface IMatchTennis {
  id?: number;
  player1Set1Result?: number;
  player2Set1Result?: number;
  player1Set2Result?: number;
  player2Set2Result?: number;
  player1Set3Result?: number;
  player2Set3Result?: number;
  round?: IRoundTennis;
  visitorPlayer?: IPlayerTennis;
  localPlayer?: IPlayerTennis;
}

export class MatchTennis implements IMatchTennis {
  constructor(
    public id?: number,
    public player1Set1Result?: number,
    public player2Set1Result?: number,
    public player1Set2Result?: number,
    public player2Set2Result?: number,
    public player1Set3Result?: number,
    public player2Set3Result?: number,
    public round?: IRoundTennis,
    public visitorPlayer?: IPlayerTennis,
    public localPlayer?: IPlayerTennis
  ) {}
}
