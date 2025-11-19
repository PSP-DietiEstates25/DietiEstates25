import { Component, input, output } from '@angular/core';

@Component({
  selector: 'app-discard-dialog',
  imports: [],
  templateUrl: './discard-dialog.component.html',
  styleUrl: './discard-dialog.component.scss',
})
export class DiscardDialogComponent {

  open = input(false);
  confirm = output();
  cancel = output();

  onBackdropClick(event: MouseEvent){
    if(event.target === event.currentTarget)
      this.onCancel();
  }

  onCancel(){
    this.cancel.emit();
  }

  onConfirm(){
    this.confirm.emit();
  }
}
