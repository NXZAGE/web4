import { Component, effect, ElementRef, inject, input, Input, signal, ViewChild } from '@angular/core';
import Konva from 'konva';
import { HitService } from '../../../../services/hit_service';

@Component({
  selector: 'target',
  templateUrl: 'target.html',
  styleUrl: 'target.scss',
})
export class Target {
  @ViewChild('canvasWrapper') canvasContainer!: ElementRef;


  radius = input.required<number>();;

  private hitService = inject(HitService);

  private readonly WIDTH = 460;
  private readonly HEIGHT = 460;
  private readonly SCALE_BASIS = 160;
  private readonly CENTER = { x: 230, y: 230 };
  private readonly DRAW_CONFIG = {
    areaColor: '#8d67ff',
    axisColor: 'white',
    fontColor: 'white',
    hitColor: 'green',
    missColor: 'red',
    fontSize: 10,
    tickSize: 4,          // Длина засечки в одну сторону
    labelOffsetX: 10,     // Смещение подписи по оси X
    labelOffsetY: 5,      // Смещение подписи по оси Y
    pointRadius: 4,
    strokeWidth: 1
  };

  private stage!: Konva.Stage;
  private layer!: Konva.Layer;

  constructor() {
    effect(() => {
      const r = this.radius();
      const hits = this.hitService.hits();
      if (this.layer) {
        this.drawVisualization(r);
      }
    });
  }

  ngAfterViewInit() {
    this.initKonva();
    this.drawVisualization(this.radius());
  }

  private initKonva() {
    this.stage = new Konva.Stage({
      container: this.canvasContainer.nativeElement,
      width: this.WIDTH,
      height: this.HEIGHT,
    });

    this.layer = new Konva.Layer();
    this.stage.add(this.layer);

    this.stage.on('click', (e) => this.handleCanvasClick(e));
  }

  // --- Математика координат ---

  private toCanvasCoords(x: number, y: number, r: number) {
    const scale = this.SCALE_BASIS / r;
    return {
      x: this.CENTER.x + x * scale,
      y: this.CENTER.y - y * scale,
    };
  }

  private toRCoords(canvasX: number, canvasY: number, r: number) {
    const scale = this.SCALE_BASIS / r;
    return {
      x: (canvasX - this.CENTER.x) / scale,
      y: (this.CENTER.y - canvasY) / scale,
    };
  }

  // --- Отрисовка ---

  private drawVisualization(r: number) {
    if (r <= 0) return;

    this.layer.destroyChildren();
    this.drawArea(r);
    this.drawAxes(r);
    this.drawExistedPoints(r);
    this.layer.draw();
  }

  private drawArea(r: number) {
    const scale = this.SCALE_BASIS / r;

    // 1. Сектор
    const wedge = new Konva.Wedge({
      x: this.CENTER.x,
      y: this.CENTER.y,
      radius: r * scale,
      angle: 90,
      rotation: 180,
      fill: this.DRAW_CONFIG.areaColor,
    });

    // 2. Прямоугольник 
    const rect = new Konva.Rect({
      x: this.CENTER.x,
      y: this.CENTER.y,
      width: r * scale,
      height: (r / 2) * scale,
      fill: this.DRAW_CONFIG.areaColor,
    });

    // 3. Треугольник 
    const pA = this.toCanvasCoords(0, 0, r);
    const pB = this.toCanvasCoords(r / 2, 0, r);
    const pC = this.toCanvasCoords(0, r, r); 

    const triangle = new Konva.Line({
      points: [pA.x, pA.y, pB.x, pB.y, pC.x, pC.y],
      fill: this.DRAW_CONFIG.areaColor,
      closed: true,
    });

    this.layer.add(wedge, rect, triangle);
  }

  private drawAxes(r: number) {
    const scale = this.SCALE_BASIS / r;
    const labels = [r, r / 2, -r / 2, -r];

    // 1. Рисуем основные линии осей (X и Y)
    const xAxis = new Konva.Line({
      points: [0, this.CENTER.y, this.WIDTH, this.CENTER.y],
      stroke: this.DRAW_CONFIG.axisColor,
      strokeWidth: this.DRAW_CONFIG.strokeWidth,
    });

    const yAxis = new Konva.Line({
      points: [this.CENTER.x, 0, this.CENTER.x, this.HEIGHT],
      stroke: this.DRAW_CONFIG.axisColor,
      strokeWidth: this.DRAW_CONFIG.strokeWidth,
    });

    this.layer.add(xAxis, yAxis);

    // 2. Рисуем засечки и подписи
    labels.forEach((val) => {
      const offset = val * scale;

      // --- Засечки и подписи на оси X ---
      const xPos = this.CENTER.x + offset;

      // Линия засечки (вертикальная на оси X)
      this.layer.add(
        new Konva.Line({
          points: [
            xPos,
            this.CENTER.y - this.DRAW_CONFIG.tickSize,
            xPos,
            this.CENTER.y + this.DRAW_CONFIG.tickSize,
          ],
          stroke: this.DRAW_CONFIG.axisColor,
          strokeWidth: this.DRAW_CONFIG.strokeWidth,
        }),
      );

      // Текст подписи X
      this.layer.add(
        new Konva.Text({
          x: xPos - this.DRAW_CONFIG.labelOffsetX,
          y: this.CENTER.y + this.DRAW_CONFIG.labelOffsetY,
          text: val.toFixed(1),
          fontSize: this.DRAW_CONFIG.fontSize,
          fill: this.DRAW_CONFIG.fontColor,
        }),
      );

      // --- Засечки и подписи на оси Y ---
      const yPos = this.CENTER.y - offset;

      // Линия засечки (горизонтальная на оси Y)
      this.layer.add(
        new Konva.Line({
          points: [
            this.CENTER.x - this.DRAW_CONFIG.tickSize,
            yPos,
            this.CENTER.x + this.DRAW_CONFIG.tickSize,
            yPos,
          ],
          stroke: this.DRAW_CONFIG.axisColor,
          strokeWidth: this.DRAW_CONFIG.strokeWidth,
        }),
      );

      // Текст подписи Y
      this.layer.add(
        new Konva.Text({
          x: this.CENTER.x + this.DRAW_CONFIG.labelOffsetY,
          y: yPos - this.DRAW_CONFIG.fontSize / 2,
          text: val.toFixed(1),
          fontSize: this.DRAW_CONFIG.fontSize,
          fill: this.DRAW_CONFIG.fontColor,
        }),
      );
    });
  }

  private drawExistedPoints(r: number) {
    this.hitService.hits().forEach((hit) => {
      // Отрисовываем точку, только если её R совпадает с текущим (как в твоем коде)
      if (hit.r === r) {
        const coords = this.toCanvasCoords(hit.x, hit.y, r);
        this.layer.add(
          new Konva.Circle({
            ...coords,
            radius: 4,
            fill: hit.isHit ? this.DRAW_CONFIG.hitColor : this.DRAW_CONFIG.missColor,
            stroke: 'black',
            strokeWidth: this.DRAW_CONFIG.strokeWidth,
          }),
        );
      }
    });
  }

  private async handleCanvasClick(e: any) {
    console.log(`r = ${this.radius()}`);
    const r = this.radius();
    if (r <= 0) return;

    const pointer = this.stage.getPointerPosition();
    if (!pointer) return;
    console.log(`${pointer.x} ${pointer.y}`);

    const coords = this.toRCoords(pointer.x, pointer.y, r);
    console.log(`${coords.x} ${coords.y}`);

    this.hitService.hit(coords.x, coords.y, r);

    this.drawExistedPoints(this.radius());
  }
}
