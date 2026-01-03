import { Component, effect, inject, OnDestroy } from '@angular/core';
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
export class StepPhotosComponent implements OnDestroy {
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

  ngOnDestroy() {
    this.clearObjectUrls();
  }

  onFilesSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    const files = Array.from(input.files ?? []);
    if (!files.length) return;

    this.facade.addImages(files);

    input.value = '';
    this.rebuildPreviews();
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
    this.clearObjectUrls();

    const existing = this.getExistingUrls();
    const files: File[] = (this.facade as any)?.images?.() ?? [];

    const fileUrls = files.map((f) => {
      const url = URL.createObjectURL(f);
      this.objectUrls.push(url);
      return url;
    });

    this.previews = [...existing, ...fileUrls];
  }

  private clearObjectUrls() {
    for (const u of this.objectUrls) URL.revokeObjectURL(u);
    this.objectUrls = [];
  }
}