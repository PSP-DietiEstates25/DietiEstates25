import { Component, computed, inject } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterModule } from '@angular/router';
import { CreateAdFacade } from './create-ad.facade';

@Component({
  selector: 'app-agent-create-layout',
  standalone: true,
  imports: [RouterLink, RouterLinkActive, RouterModule],
  templateUrl: './create-layout.component.html',
})
export class AgentCreateLayoutComponent {
  
  private facade = inject(CreateAdFacade);

  basicsValid = computed(() => !!this.facade.basics());
  detailsValid = computed(
    () => !!(this.facade.utility() && this.facade.geographicalPosition())
  );
  photosValid = computed(() => this.facade.images().length > 0);
  allValid = computed(() => this.facade.allValid());

steps = [
  { key: 'basics',    label: 'Dati principali', valid: this.basicsValid },
  { key: 'details',   label: 'Dettagli',        valid: this.detailsValid },
  { key: 'cadastral', label: 'Dati catastali',       valid: computed(() => !!this.facade.cadastralData()) },
  { key: 'photos',    label: 'Foto',            valid: this.photosValid },
  { key: 'review',    label: 'Riepilogo',       valid: this.allValid },
] as const;
}
