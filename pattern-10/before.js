// 변경 전: '데이터/처리' 같은 추상적/모호한 용어 남발
function processData(d){
  // 도메인은 주문/결제인데, 전혀 드러나지 않음
  return d.map(x => x.v).reduce((a,b)=>a+b,0);
}
