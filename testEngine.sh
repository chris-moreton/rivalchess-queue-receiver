cutechess-cli -engine cmd="java -jar /home/chrismoreton/Chess/$1" -engine cmd="java -jar /home/chrismoreton/Chess/rivalchess-34.0.3-1.jar" -each proto=uci book="/home/chrismoreton/Chess/ProDeo.bin" timemargin=1500 st=0.25 -resign movecount=10 score=600 -rounds 2 -pgnout /home/chrismoreton/Chess/test.pgn -epdout /home/chrismoreton/Chess/test.epd -debug

