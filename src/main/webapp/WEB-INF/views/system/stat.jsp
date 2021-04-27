<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.8.0/Chart.min.css">
<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.8.0/Chart.js"></script>
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<style>
#moveChart {
   	position: absolute;
    left: 50%;
    top: 0;
}
</style>
<script>
$(function() {
	window.chartColors = {
		red: 'rgb(255, 99, 132)',
		orange: 'rgb(255, 159, 64)',
		yellow: 'rgb(255, 205, 86)',
		green: 'rgb(75, 192, 192)',
		blue: 'rgb(54, 162, 235)',
		purple: 'rgb(153, 102, 255)',
		grey: 'rgb(201, 203, 207)'
	};
	var ctx1 = document.getElementById('chart1').getContext('2d');
	var ctx2 = document.getElementById('chart2').getContext('2d');
	var ctx3 = document.getElementById('chart3').getContext('2d');
	var ctx4 = document.getElementById('chart4').getContext('2d');

			// 1번 차트 : 차트 종류, 차트 내용(항목, 데이터, 배경색, 보더색, 보더굵기)
            var myChart1 = new Chart(ctx1, {
                type: 'bar',
                data: {
                    labels: ['1월', '2월', '3월', '4월', '5월', '6월'],
                    datasets: [{
                        label: '수익금',
                        data: [21600, 31600, 45200, 25400, 13200, 15300],
                        backgroundColor: ['rgba(255, 99, 132, 0.2)', 'rgba(54, 162, 235, 0.2)', 'rgba(255, 206, 86, 0.2)',
                            'rgba(75, 192, 192, 0.2)', 'rgba(153, 102, 255, 0.2)', 'rgba(255, 159, 64, 0.2)'
                        ],
                        borderColor: ['rgba(255, 99, 132, 1)', 'rgba(54, 162, 235, 1)', 'rgba(255, 206, 86, 1)',
                            'rgba(75, 192, 192, 1)', 'rgba(153, 102, 255, 1)', 'rgba(255, 159, 64, 1)'
                        ],
                        borderWidth: 1
                    }]
                },
                options: {
                    scales: {
                        yAxes: [{
                            ticks: {
                                beginAtZero: true
                            }
                        }]
                    }
                }
            });

            var myChart2 = new Chart(ctx2, {
                type: 'line',
                data: {
                    labels: ['1/4', '2/4', '3/4', '4/4'],
                    datasets: [{
                        label: '작년 매출액',
                        data: [323, 411, 384, 377],
                        fill: false,
                        backgroundColor: window.chartColors.red,
                        borderColor: window.chartColors.red
                    }, {
                        label: '금년 매출액',
                        data: [342, 378, 384, 335],
                        fill: false,
                        backgroundColor: window.chartColors.blue,
                        borderColor: window.chartColors.blue
                    }]
                },
                options: {
                    responsive: true,
                    title: {
                        display: true,
                        fontSize: 20,
                        text: '작년/금년 분기별 매출액 비교'
                    },
                    tooltips: {
                        mode: 'index',
                        intersect: false,
                    },
                    hover: {
                        mode: 'nearest',
                        intersect: true
                    },
                    scales: {
                        xAxes: [{
                            display: true,
                            ticks: {
                                fontSize: 16
                            },
                            scaleLabel: {
                                display: true,
                                labelString: '분기',
                                fontSize: 16
                            }
                        }],
                        yAxes: [{
                            display: true,
                            ticks: {
                                min: 250,
                                max: 450,
                                stepSize: 50,
                                fontSize: 16
                            },
                            scaleLabel: {
                                display: true,
                                labelString: '매출액',
                                fontSize: 16
                            }
                        }]
                    }
                }
            });

            console.log(ctx3)

            var myChart3 = new Chart(ctx3, {
                type: 'doughnut',
                data: {
                    labels: ['서울', '경기', '인천', '충남', '부산'],
                    datasets: [{
                        label: '수익금',
                        data: [70, 60, 50, 100, 30],
                        backgroundColor: [window.chartColors.red,
                            window.chartColors.orange,
                            window.chartColors.yellow,
                            window.chartColors.green,
                            window.chartColors.blue,
                        ]
                    }]
                },
                options: {
                    responsive: true,
                    legend: {
                        position: 'top',
                    },
                    title: {
                        display: true,
                        text: '지역별 지점 현황'
                    },
                    animation: {
                        animateScale: true,
                        animateRotate: true
                    }
                }
            });

            var DATA_COUNT = 16;
            var MIN_XY = -150;
            var MAX_XY = 100;

            var utils = utils = {
                srand: function(seed) {
                    this._seed = seed;
                },

                rand: function(min, max) {
                    var seed = this._seed;
                    min = min === undefined ? 0 : min;
                    max = max === undefined ? 1 : max;
                    this._seed = (seed * 9301 + 49297) % 233280;
                    return min + (this._seed / 233280) * (max - min);
                },

                numbers: function(config) {
                    var cfg = config || {};
                    var min = cfg.min || 0;
                    var max = cfg.max || 1;
                    var from = cfg.from || [];
                    var count = cfg.count || 8;
                    var decimals = cfg.decimals || 8;
                    var continuity = cfg.continuity || 1;
                    var dfactor = Math.pow(10, decimals) || 0;
                    var data = [];
                    var i, value;

                    for (i = 0; i < count; ++i) {
                        value = (from[i] || 0) + this.rand(min, max);
                        if (this.rand() <= continuity) {
                            data.push(Math.round(dfactor * value) / dfactor);
                        } else {
                            data.push(null);
                        }
                    }

                    return data;
                },

                labels: function(config) {
                    var cfg = config || {};
                    var min = cfg.min || 0;
                    var max = cfg.max || 100;
                    var count = cfg.count || 8;
                    var step = (max - min) / count;
                    var decimals = cfg.decimals || 8;
                    var dfactor = Math.pow(10, decimals) || 0;
                    var prefix = cfg.prefix || '';
                    var values = [];
                    var i;

                    for (i = min; i < max; i += step) {
                        values.push(prefix + Math.round(dfactor * i) / dfactor);
                    }

                    return values;
                },

                months: function(config) {
                    var cfg = config || {};
                    var count = cfg.count || 12;
                    var section = cfg.section;
                    var values = [];
                    var i, value;

                    for (i = 0; i < count; ++i) {
                        value = MONTHS[Math.ceil(i) % 12];
                        values.push(value.substring(0, section));
                    }

                    return values;
                },

                color: function(index) {
                    var COLORS = [
                        '#4dc9f6',
                        '#f67019',
                        '#f53794',
                        '#537bc4',
                        '#acc236',
                        '#166a8f',
                        '#00a950',
                        '#58595b',
                        '#8549ba'
                    ];
                    return COLORS[index % COLORS.length];
                },

                transparentize: function(color, opacity) {
                    var alpha = opacity === undefined ? 0.5 : 1 - opacity;
                    return Color(color).alpha(alpha).rgbString();
                }
            };

            utils.srand(110);

            function colorize(opaque, context) {
                var value = context.dataset.data[context.dataIndex];
                var x = value.x / 100;
                var y = value.y / 100;
                var r = x < 0 && y < 0 ? 250 : x < 0 ? 150 : y < 0 ? 50 : 0;
                var g = x < 0 && y < 0 ? 0 : x < 0 ? 50 : y < 0 ? 150 : 250;
                var b = x < 0 && y < 0 ? 0 : x > 0 && y > 0 ? 250 : 150;
                var a = opaque ? 1 : 0.5 * value.v / 1000;

                return 'rgba(' + r + ',' + g + ',' + b + ',' + a + ')';
            }

            function generateData() {
                var data = [];
                var i;

                for (i = 0; i < DATA_COUNT; ++i) {
                    data.push({
                        x: utils.rand(MIN_XY, MAX_XY),
                        y: utils.rand(MIN_XY, MAX_XY),
                        v: utils.rand(0, 1000)
                    });
                }

                return data;
            }

            var data = {
                datasets: [{
                    data: generateData()
                }, {
                    data: generateData()
                }]
            };

            var options = {
                aspectRatio: 1,
                legend: false,
                tooltips: false,

                elements: {
                    point: {
                        backgroundColor: colorize.bind(null, false),

                        borderColor: colorize.bind(null, true),

                        borderWidth: function(context) {
                            return Math.min(Math.max(1, context.datasetIndex + 1), 8);
                        },

                        hoverBackgroundColor: 'transparent',

                        hoverBorderColor: function(context) {
                            return utils.color(context.datasetIndex);
                        },

                        hoverBorderWidth: function(context) {
                            var value = context.dataset.data[context.dataIndex];
                            return Math.round(8 * value.v / 1000);
                        },

                        radius: function(context) {
                            var value = context.dataset.data[context.dataIndex];
                            var size = context.chart.width;
                            var base = Math.abs(value.v) / 1000;
                            return (size / 24) * base;
                        }
                    }
                }
            };

            var myChart4 = new Chart(ctx4, {
                type: 'bubble',
                data: data,
                options: options
            });

            function randomize() {
                chart.data.datasets.forEach(function(dataset) {
                    dataset.data = generateData();
                });
                chart.update();
            }

            function addDataset() {
                chart.data.datasets.push({
                    data: generateData()
                });
                chart.update();
            }

            function removeDataset() {
                chart.data.datasets.shift();
                chart.update();
            }
        });
    </script>

	<!-- 차트를 출력할 canvas 요소 4개 생성. 레이아웃은 w3.css 사용 -->
    <body>
        <div class="w3-panel w3-card-4 myChart">
            <canvas id="chart1"></canvas>
        </div>
        <div class="w3-panel w3-card-4 myChart">
            <canvas id="chart2"></canvas>
        </div>
        <div class="w3-panel w3-card-4 myChart">
            <canvas id="chart3"></canvas>
        </div>
		<div class="w3-panel w3-card-4 myChart">
            <canvas id="chart4"></canvas>
        </div>
    </body>
</html>