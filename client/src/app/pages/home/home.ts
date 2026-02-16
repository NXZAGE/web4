import { Component } from "@angular/core";
import { PanelModule } from "primeng/panel";
import { ButtonModule } from "primeng/button";
import { Target } from "./components/target/target";
import { HitForm } from "./components/hit_form/hit_form";
import { HitTable } from "./components/hit_table/hit_table";
@Component({
  selector: "home",
  templateUrl: "home.html",
  styleUrl: "home.scss",
  imports: [PanelModule, ButtonModule, HitForm, Target, HitTable]
})
export class Home {
  helloMessage = "Hello world!";
};