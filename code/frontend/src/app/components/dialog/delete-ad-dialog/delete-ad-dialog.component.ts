import { Component, input, output } from '@angular/core';

@Component({
  selector: 'app-delete-ad-dialog',
  imports: [],
  templateUrl: './delete-ad-dialog.component.html',
  styleUrl: './delete-ad-dialog.component.scss',
})
export class DeleteAdDialogComponent {
  open = input(false);
  confirm = output();
  cancel = output();

  onBackdropClick(event: MouseEvent) {
    if (event.target === event.currentTarget) this.onCancel();
  }

  onCancel() {
    this.cancel.emit();
  }

  onConfirm() {
    this.confirm.emit();
  }
}
