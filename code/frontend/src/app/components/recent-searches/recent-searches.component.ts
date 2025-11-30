import { Component, inject, signal, computed } from '@angular/core';
import { DatePipe } from '@angular/common';
import {
  SearchFacade,
  RecentSearchSnapshot,
} from '../../components/search/search.facade';

@Component({
  selector: 'app-recent-searches',
  standalone: true,
  imports: [DatePipe],
  templateUrl: './recent-searches.component.html',
})
export class RecentSearchesComponent {

  facade = inject(SearchFacade);

  readonly pageSize = 3;
  public start = signal(0);

  total = computed(() => this.facade.recent().length);
  canPrev = computed(() => this.start() > 0);
  canNext = computed(() => this.start() + this.pageSize < this.total());
  math = Math;

  visible = computed<RecentSearchSnapshot[]>(() =>
    this.facade.recent().slice(this.start(), this.start() + this.pageSize)
  );

  prev() {
    if (!this.canPrev()) return;
    this.start.set(Math.max(0, this.start() - this.pageSize));
  }
  
  next() {
    if (!this.canNext()) return;
    this.start.set(
      Math.min(
        Math.max(0, this.total() - this.pageSize),
        this.start() + this.pageSize
      )
    );
  }

  replay = (search: RecentSearchSnapshot) => this.facade.replaySearch(search);

  remove = (search: RecentSearchSnapshot) => {
    this.facade.removeRecent(search.id);
    if (this.start() >= this.total())
      this.start.set(Math.max(0, this.total() - this.pageSize));
  };

  clearAll = () => {
    this.facade.forgetRecent();
    this.start.set(0);
  };
}
