import { Component, computed, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  Router,
  RouterLink,
  RouterLinkActive,
  ActivatedRoute,
  RouterModule,
} from '@angular/router';
import { AdDraftService } from '../../vecchioService/ad-draft.service';

@Component({
  selector: 'app-agent-create-layout',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive, RouterModule],
  templateUrl: './create-layout.component.html',
})
export class AgentCreateLayoutComponent {
  private draft = inject(AdDraftService);
  steps = [
    { key: 'basics', label: 'Dati principali', valid: this.draft.basicsValid },
    { key: 'details', label: 'Dettagli', valid: this.draft.detailsValid },
    { key: 'photos', label: 'Foto', valid: this.draft.photosValid },
    {
      key: 'review',
      label: 'Riepilogo',
      valid: computed(() => this.draft.allValid()),
    },
  ] as const;
}
