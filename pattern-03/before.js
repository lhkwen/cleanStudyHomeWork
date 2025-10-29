// 변경 전: 이름이 실제 의미를 왜곡
const accountList = { // 실제로는 리스트가 아니라 키-값 객체(Map 유사)
  A123: {balance: 100},
  B456: {balance: 250},
};
// 헷갈리는 문자(l, O)는 피해야 하지만 아래는 사용 중
const O = 0; 
const l = 1; 
function getBalanceFromList(id) { // List 아님
  return accountList[id]?.balance ?? O;
}
console.log(getBalanceFromList("A123"), l);
