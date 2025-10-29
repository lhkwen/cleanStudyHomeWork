// 변경 후: 의도가 드러나는 이름
const elapsedTimeInDays = 3;
function getFlaggedCells(cells) {
  return cells.filter(cell => cell.isFlagged === true);
}
console.log(getFlaggedCells([{isFlagged:true},{isFlagged:false}])); // [ { isFlagged: true } ]
