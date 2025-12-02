import { Component, input, output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-search-bar',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './search-bar.component.html',
  styleUrl: './search-bar.component.scss',
})
export class SearchBarComponent {
  placeholder = input<string>('Città, indirizzo');

  search = output<string>();

  q = '';

  onInput(v: string) {
    this.q = v;
  }
  onSubmit() {
    this.search.emit(this.q?.trim() ?? '');
  }
}
