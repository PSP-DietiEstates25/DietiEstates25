import { Component, computed, inject, OnInit } from '@angular/core';
import {
  RouterLink,
  RouterLinkActive,
  RouterModule,
  ActivatedRoute,
  Router,
} from '@angular/router';
import { EditAdFacade } from './edit-ad.facade';
import { CreateAdFacade } from '../create-ad/create-ad.facade';

@Component({
  selector: 'app-agent-edit-layout',
  standalone: true,
  imports: [RouterLink, RouterLinkActive, RouterModule],
  templateUrl: './edit-layout.component.html',
})
export class EditLayoutComponent implements OnInit {
  private facade = inject(EditAdFacade);
  private activatedRoute = inject(ActivatedRoute);
  private routerService = inject(Router);

  basicsValid = computed(() => !!this.facade.basics());
  detailsValid = computed(
    () => !!(this.facade.utility() && this.facade.geographicalPosition())
  );
  photosValid = computed(() => this.facade.images().length > 0);
  allValid = computed(() => this.facade.allValid());

  realestateId!: number;

  steps = [
    { key: 'basics', label: 'Dati principali', valid: this.basicsValid },
    { key: 'details', label: 'Dettagli', valid: this.detailsValid },
    {
      key: 'cadastraldata',
      label: 'Catastali',
      valid: computed(() => !!this.facade.cadastralData()),
    },
    { key: 'photos', label: 'Foto', valid: this.photosValid },
    { key: 'review', label: 'Riepilogo', valid: this.allValid },
  ] as const;

  ngOnInit(): void {
    const idFromChild =
      this.activatedRoute.snapshot.firstChild?.paramMap.get('realestateId');

    const realestateId = idFromChild ? Number(idFromChild) : NaN;

    this.realestateId = realestateId;
    (this.facade as any).load(realestateId);
  }

  cancelEdit() {
    this.routerService.navigate(['/agent']);
  }
}
