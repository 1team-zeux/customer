import os
import psycopg2
from fastapi import APIRouter
from opentelemetry import trace

router = APIRouter()
tracer = trace.get_tracer("recommendation-agent")

DATABASE_URL = os.getenv("DATABASE_URL", "postgresql://demo:demo@postgres:5432/demo")


def get_conn():
    return psycopg2.connect(DATABASE_URL)


@router.get("/recommendations/{user_id}")
def get_recommendations(user_id: str):
    with tracer.start_as_current_span("recommendation.query"):
        conn = None
        try:
            conn = get_conn()
            cur = conn.cursor()
            cur.execute("SELECT id, name FROM products ORDER BY RANDOM() LIMIT 3")
            rows = cur.fetchall()
            cur.close()
            return {
                "userId": user_id,
                "recommendations": [{"productId": r[0], "name": r[1]} for r in rows]
            }
        except Exception as e:
            return {"userId": user_id, "recommendations": [], "error": str(e)}
        finally:
            if conn:
                conn.close()
