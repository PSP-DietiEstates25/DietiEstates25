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
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  basicsValid = computed(() => !!this.facade.basics());
  detailsValid = computed(
    () => !!(this.facade.utilities() && this.facade.position())
  );
  photosValid = computed(() => this.facade.images().length > 0);
  allValid = computed(() => this.facade.allValid());

  realestateId!: number;

  steps = [
    { key: 'basics', label: 'Dati principali', valid: this.basicsValid },
    { key: 'details', label: 'Dettagli', valid: this.detailsValid },
    {
      key: 'cadastral',
      label: 'Catastali',
      valid: computed(() => !!this.facade.cadastral()),
    },
    { key: 'photos', label: 'Foto', valid: this.photosValid },
    { key: 'review', label: 'Riepilogo', valid: this.allValid },
  ] as const;

  ngOnInit(): void {
    this.realestateId = Number(
      this.route.snapshot.paramMap.get('realestateId')
    );
    (this.facade as any).load(this.realestateId);
  }

  cancelEdit() {
    this.router.navigate(['/agent']);
  }
}
