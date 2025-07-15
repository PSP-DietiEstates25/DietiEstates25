import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-search-bar',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './search-bar.component.html',
  styleUrls: ['./search-bar.component.scss']
})
export class SearchBarComponent {
  location = '';
  suggestions = ['Naples', 'Rome', 'Milan'];

  get filteredSuggestions(): string[] {
    return this.suggestions.filter(s => s.toLowerCase().includes(this.location.toLowerCase()));
  }
}