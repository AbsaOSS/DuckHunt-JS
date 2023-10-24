import { HttpClient } from '@angular/common/http';
import { Component, OnInit, OnDestroy, Inject } from '@angular/core';
import { Observable, Subscription, timer } from 'rxjs';
import { APP_SETTINGS, AppSettings } from './settings';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit, OnDestroy {
  scores: Score[] = []
  displayedColumns: string[] = ['email', 'score'];
  private subscription: Subscription | undefined;
  everySecond: Observable<number> = timer(0, 5000);
  settings: AppSettings
  constructor(
    @Inject(APP_SETTINGS) settings: AppSettings, 
    private http: HttpClient
    ) { 
      this.settings = settings
    }

  ngOnInit(): void {
    console.info("gnOnInit")
    this.subscription = this.everySecond.subscribe((seconds) => {
      console.info("in subscription")
      this.getData()
      })
  }

  getData() {
    console.info(this.settings.apiBaseUrl)
    /*
    this.http.get<QueryResult>('https://afgesl44kuvsiwzkmo6ku3ml2i0bhnew.lambda-url.af-south-1.on.aws/ksql?query=select%20*%20from%20SCORES_TABLE_SUMMARY')
      .subscribe(data => {
        console.info(data)
        let scores = []
        for (let index = 1; index < data.results.length; index++) {
          const element = data.results[index];
          scores.push(new Score(element[0].toString(), element[1] as Number))
        }

        this.scores = scores.sort(this.compareScore) //.slice(0, 10)
        console.info(this.scores)
      });
      */
  }

  compareScore(a: Score, b: Score): number {
    if (a.score < b.score) return 1
    if (a.score > b.score) return -1
    return 0
  }
  title = 'scoreboard';

  ngOnDestroy(): void {
    this.subscription?.unsubscribe();
  }

}
export class Score {
  email: String
  score: Number

  constructor(email: String, score: Number) {
    this.email = email
    this.score = score
  }

}
export interface Query {
  columnNames: String[]
  columnTypes: String[]
  queryId: String
}
export interface QueryResult {
  query: Query
  results: Object[][]
}