import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'tournament-tennis',
        //loadChildren: () => import('./tournament-tennis/tournament-tennis.module').then(m => m.TennisJazzTournamentTennisModule)
        loadChildren: './tournament-tennis/tournament-tennis.module#TennisJazzTournamentTennisModule'
      },
      {
        path: 'tournament-group-tennis',
        //loadChildren: () => import('./tournament-group-tennis/tournament-group-tennis.module').then(m => m.TennisJazzTournamentGroupTennisModule)
        loadChildren: './tournament-group-tennis/tournament-group-tennis.module#TennisJazzTournamentGroupTennisModule'
      },
      {
        path: 'player-tennis',
        //loadChildren: () => import('./player-tennis/player-tennis.module').then(m => m.TennisJazzPlayerTennisModule)
        loadChildren: './player-tennis/player-tennis.module#TennisJazzPlayerTennisModule'
      },
      {
        path: 'round-tennis',
        //loadChildren: () => import('./round-tennis/round-tennis.module').then(m => m.TennisJazzRoundTennisModule)
        loadChildren: './round-tennis/round-tennis.module#TennisJazzRoundTennisModule'
      },
      {
        path: 'match-tennis',
        //loadChildren: () => import('./match-tennis/match-tennis.module').then(m => m.TennisJazzMatchTennisModule)
        loadChildren: './match-tennis/match-tennis.module#TennisJazzMatchTennisModule'
      },
      {
        path: 'ranking-tennis',
        //loadChildren: () => import('./ranking-tennis/ranking-tennis.module').then(m => m.TennisJazzRankingTennisModule)
        loadChildren: './ranking-tennis/ranking-tennis.module#TennisJazzRankingTennisModule'
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
