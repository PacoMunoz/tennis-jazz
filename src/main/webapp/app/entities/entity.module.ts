import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'tournament-tennis',
        loadChildren: './tournament-tennis/tournament-tennis.module#TennisJazzTournamentTennisModule'
      },
      {
        path: 'tournament-group-tennis',
        loadChildren: './tournament-group-tennis/tournament-group-tennis.module#TennisJazzTournamentGroupTennisModule'
      },
      {
        path: 'player-tennis',
        loadChildren: './player-tennis/player-tennis.module#TennisJazzPlayerTennisModule'
      },
      {
        path: 'round-tennis',
        loadChildren: './round-tennis/round-tennis.module#TennisJazzRoundTennisModule'
      },
      {
        path: 'match-tennis',
        loadChildren: './match-tennis/match-tennis.module#TennisJazzMatchTennisModule'
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ],
  declarations: [],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TennisJazzEntityModule {}
