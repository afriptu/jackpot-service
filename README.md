```markdown
# Jackpot Service


How to run
1. Build and run:
   mvn spring-boot:run  
   
2. Application runs on http://localhost:8080


API
1) Publish a bet (simulates producing to Kafka)
POST /api/bets
Content-Type: application/json
Body:
{
  "betId": 101,
  "userId": 12345,
  "jackpotId": 1,
  "betAmount": 10.00
}

2) Evaluate a bet for jackpot win
POST /api/jackpots/evaluate
Content-Type: application/json
Body:
{
  "betId": 101,
  "userId": 12345,
  "jackpotId": 1
}

```