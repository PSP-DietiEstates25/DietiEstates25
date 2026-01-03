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
  private objectUrls: string[] = [];

  isDiscardModalOpen = false;

  constructor() {
    effect(() => this.rebuildPreviews());
  }

  onFilesSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    const files = Array.from(input.files ?? []);
    if (!files.length) return;

    const anyFacade = this.facade as any;
    const isEdit = anyFacade?.mode === 'edit';

    if (isEdit && typeof anyFacade?.existingImageUrls === 'function') {
      anyFacade.existingImageUrls.set([]);
      if (typeof anyFacade?.setImages === 'function') {
        anyFacade.setImages(files);
      } else {
        this.facade.setImages(files);
      }

      this.toastrService.info(
        'Nuove immagini caricate: sostituiranno quelle esistenti al salvataggio.',
        'Immagini',
      );
    } else {
      // create: aggiungo
      this.facade.addImages(files);
    }

    input.value = '';
    this.rebuildPreviews();
  }

  remove(index: number) {
    const existingCount = this.getExistingUrls().length;

    if (index < existingCount) {
      this.toastrService.info(
        'Per cambiare le immagini esistenti, carica un nuovo set di immagini.',
        'Info',
      );
      return;
    }

    this.facade.removeImage(index - existingCount);
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
    (this.facade as any)?.clearSavedData?.();
    this.routerService.navigate(['/']);
    this.toastrService.error('Operazione interrotta!', 'Cancellazione');
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

  private getExistingUrls(): string[] {
    const anyFacade = this.facade as any;
    return anyFacade?.existingImageUrls?.() ?? [];
  }

  private rebuildPreviews() {
    for (const u of this.objectUrls) URL.revokeObjectURL(u);
    this.objectUrls = [];

    const existing = this.getExistingUrls();
    const files = this.facade.images() ?? [];

    const fileUrls = files.map((f) => {
      const url = URL.createObjectURL(f);
      this.objectUrls.push(url);
      return url;
    });

    this.previews = [...existing, ...fileUrls];
  }
}
