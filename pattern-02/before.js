// 변경 전: 의도가 드러나지 않는 이름
const d = 3; // days? duration? deadline?
function getThem(data) {
  return data.filter(x => x.flag === true);
}
console.log(getThem([{flag:true},{flag:false}])); // ???
