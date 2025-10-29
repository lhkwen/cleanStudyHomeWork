// 변경 후: 정확한 의미 전달
const accountMapById = {
  A123: {balance: 100},
  B456: {balance: 250},
};
const ZERO = 0;
const ONE = 1;
function getBalanceByAccountId(accountId) {
  return accountMapById[accountId]?.balance ?? ZERO;
}
console.log(getBalanceByAccountId("A123"), ONE);
