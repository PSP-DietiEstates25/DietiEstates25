import { Component, inject, input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SearchService } from '../../../services/services/search.service';

@Component({
  selector: 'app-search-bar',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './search-bar.component.html',
  styleUrl: './search-bar.component.scss',
})
export class SearchBarComponent {
  placeholder = input<string>('Città, indirizzo');
  private search = inject(SearchService);

  get q() {
    return this.search.q();
  }
  onInput(v: string) {
    this.search.setQuery(v);
  }
  onSubmit() {
    this.search.goToSearch();
  }
    
}
