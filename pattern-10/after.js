// 변경 후: 해법 영역(Queue/Worker) + 문제 영역(Order/Payment)을 적절히 혼합
class JobQueue {
  constructor(){ this.jobs = []; }
  enqueue(job){ this.jobs.push(job); }
  dequeue(){ return this.jobs.shift(); }
}

class Order {
  constructor(items){ this.items = items; }
  totalAmount(){ return this.items.map(i=>i.price).reduce((a,b)=>a+b,0); }
}

function charge(order, paymentGateway){
  return paymentGateway.pay(order.totalAmount());
}

// 사용 예시
const queue = new JobQueue();
queue.enqueue(() => {
  const order = new Order([{price:10}, {price:20}]);
  return charge(order, { pay: (amt)=>`PAID:${amt}` });
});
const job = queue.dequeue();
console.log(job()); // PAID:30
