import { Component } from "@angular/core";
import { PanelModule } from "primeng/panel";
import { ButtonModule } from "primeng/button";
@Component({
  selector: "home",
  templateUrl: "home.html",
  styleUrl: "home.scss",
  imports: [PanelModule, ButtonModule]
})
export class Home {
  helloMessage = "Hello world!";
};