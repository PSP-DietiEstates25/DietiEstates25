import { Component, effect, inject } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { CreateAdFacade } from './create-ad.facade';
import { ToastrService } from 'ngx-toastr';
import { DiscardDialogComponent } from '../dialog/discard-dialog/discard-dialog.component';

@Component({
  selector: 'app-step-photos',
  standalone: true,
  imports: [DiscardDialogComponent],
  templateUrl: './step-photos.component.html',
})
export class StepPhotosComponent {
  private activatedRoute = inject(ActivatedRoute);
  private toastrService = inject(ToastrService);
  private facade = inject(CreateAdFacade);
  private routerService = inject(Router);

  previews: string[] = [];
  isDiscardModalOpen = false;

  constructor() {
    effect(() => {
      const imgs = this.facade.getImages();
      this.clearPreviews();
      this.previews = (imgs ?? []).map((file) => URL.createObjectURL(file));
    });
  }

  ngOnInit() {
    this.rebuildPreviews();
  }

  ngOnDestroy() {
    this.clearPreviews();
  }

  onFilesSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    const files = Array.from(input.files ?? []);
    if (!files.length) return;

    if (typeof (this.facade as any).addImages === 'function') {
      (this.facade as any).addImages(files);
    } else {
      for (const file of files) this.facade.addImages(files);
    }

    this.rebuildPreviews();
    input.value = '';
  }

  remove(index: number) {
    this.facade.removeImage(index);
    this.rebuildPreviews();
  }

  openDiscardModal() {
    this.isDiscardModalOpen = true;
  }

  closeDiscardModal() {
    this.isDiscardModalOpen = false;
  }

  confirmDiscard() {
    this.closeDiscardModal();
    this.facade.clearSavedData();
    this.routerService.navigate(['/']);
    this.toastrService.error('Creazione annuncio interrotta!', 'Cancellazione');
  }

  previous() {
    this.routerService.navigate(['../cadastraldata'], {
      relativeTo: this.activatedRoute,
    });
  }

  next() {
    this.routerService.navigate(['../review'], {
      relativeTo: this.activatedRoute,
    });
  }

  private rebuildPreviews() {
    this.clearPreviews();
    this.previews = (this.facade.images() ?? []).map((file) =>
      URL.createObjectURL(file)
    );
  }

  private clearPreviews() {
    for (const url of this.previews) URL.revokeObjectURL(url);
    this.previews = [];
  }
}
