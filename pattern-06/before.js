// 변경 전: 헝가리안/접두어 남용
const strUserNm = "Alice";
let m_nCount = 3;
class IShapeFactory { // 인터페이스 접두/접미 남용
  createCircle(){ /* ... */ }
}
class ShapeFactoryImpl extends IShapeFactory {
  createCircle(){ /* ... */ }
}
