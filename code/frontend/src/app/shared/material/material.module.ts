import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';

import { NgModule } from '@angular/core';

@NgModule({
  imports: [MatFormFieldModule, MatSelectModule],
  exports: [MatFormFieldModule, MatSelectModule]
})
export class MaterialModule {}
