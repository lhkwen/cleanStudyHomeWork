// 변경 후: 필요한 만큼만 맥락 제공 + 군더더기 제거
const address = {
  street: "1 Main",
  state: "CA", // Address.state라면 의미가 분명
};
// 접두어 남용 제거
const accountAddress = { street: "1 Main", state: "CA" };
