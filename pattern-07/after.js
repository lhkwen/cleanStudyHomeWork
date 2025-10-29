// 변경 후: 클래스는 명사, 메서드는 동사
class UserService {
  save(user){ /* ... */ }
  delete(userId){ /* ... */ }
}
const service = new UserService();
service.save({id:1, name:'Lee'});
