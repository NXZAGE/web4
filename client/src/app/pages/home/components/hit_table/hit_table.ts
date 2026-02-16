import { Component, inject } from "@angular/core";
import { TableModule } from "primeng/table"
import { Hit, HitService } from "../../../../services/hit_service";

@Component({
	selector: "hit-table",
	templateUrl: "hit_table.html",
	styleUrl: "hit_table.scss",
	imports : [
		TableModule
	]
})
export class HitTable {
	private hitService = inject(HitService);
	readonly hits = this.hitService.hits;
}
