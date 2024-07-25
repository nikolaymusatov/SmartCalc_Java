#include "model.h"

using namespace myNamespace;

Model::Model() {}

double Model::getResult() const { return result; }

Model::graph_type Model::getGraphData() const { return graphData; }

void Model::calculateValue(std::string& expression, double x) {
  std::queue<Token> rpnTokens = process(expression);
  result = calculate(&rpnTokens, x);
}

void Model::calculateGraph(std::string& expression, double xmin, double xmax) {
  std::queue<Token> rpnTokens = process(expression);
  std::queue<Token> tmp;
  graphData.first.clear();
  graphData.second.clear();
  double X = xmin, h = (xmax - xmin) / 500;
  while (X <= xmax) {
    tmp = rpnTokens;
    graphData.first.push_back(X);
    graphData.second.push_back(calculate(&tmp, X));
    X += h;
  }
}

std::queue<Token> Model::process(std::string& expression) {
  std::queue<Token> tokens;
  std::queue<Token> rpnTokens;
  parse(expression, &tokens);
  check(&tokens);
  convert(&tokens, &rpnTokens);
  return rpnTokens;
}

void Model::parse(std::string& expression, std::queue<Token>* tokens) {
  size_t index = 0;
  size_t oParanthQnt = 0, cParanthQnt = 0;
  std::string token;
  const std::map<std::string, Token> token_map = setTokenMap();
  while (index < expression.size()) {
    if (isalpha(expression[index])) {
      token = readFunction(expression, index);
    } else if (isdigit(expression[index])) {
      token = readNumber(expression, index);
    } else {
      token = expression[index];
      index++;
    }
    if (token == "(") oParanthQnt++;
    if (token == ")") cParanthQnt++;
    if (token != " ") pushToken(token, tokens, token_map);
  }
  if (oParanthQnt != cParanthQnt) throw std::string("check brackets!");
}

std::map<std::string, Token> Model::setTokenMap() {
  const std::map<std::string, Token> token_map{
      {"x", Token("x", 0, p1, tNum, nullptr)},
      {"+", Token("+", 0, p2, tBinOperator, std::plus<double>())},
      {"-", Token("-", 0, p2, tBinOperator, std::minus<double>())},
      {"*", Token("*", 0, p3, tBinOperator, std::multiplies<double>())},
      {"/", Token("/", 0, p3, tBinOperator, std::divides<double>())},
      {"mod", Token("mod", 0, p3, tBinOperator, fmodl)},
      {"^", Token("^", 0, p4, tBinOperator, powl)},
      {"cos", Token("cos", 0, p5, tFunc, cosl)},
      {"sin", Token("sin", 0, p5, tFunc, sinl)},
      {"tan", Token("tan", 0, p5, tFunc, tanl)},
      {"acos", Token("acos", 0, p5, tFunc, acosl)},
      {"asin", Token("asin", 0, p5, tFunc, asinl)},
      {"atan", Token("atan", 0, p5, tFunc, atanl)},
      {"sqrt", Token("sqrt", 0, p5, tFunc, sqrtl)},
      {"ln", Token("ln", 0, p5, tFunc, logl)},
      {"log", Token("log", 0, p5, tFunc, log10l)},
      {"(", Token("(", 0, p0, tOpenParanth, nullptr)},
      {")", Token(")", 0, p0, tClosedParanth, nullptr)}};
  return token_map;
}

std::string Model::readFunction(std::string& expression, size_t& index) {
  std::regex word_regex("([a-z]+)");
  std::sregex_iterator regex_iter = std::sregex_iterator(
      expression.begin() + index, expression.end(), word_regex);
  std::smatch match = *regex_iter;
  index += match.length();
  return match.str();
}

std::string Model::readNumber(std::string& expression, size_t& index) {
  std::regex number_regex("\\d+([.]\\d+)?(e([-+])?\\d+)?");
  std::sregex_iterator regex_iter = std::sregex_iterator(
      expression.begin() + index, expression.end(), number_regex);
  std::smatch match = *regex_iter;
  index += match.length();
  return match.str();
}

void Model::pushToken(std::string& token_name, std::queue<Token>* tokens,
                      const std::map<std::string, Token>& token_map) {
  if (std::isdigit(token_name[0])) {
    tokens->push(Token("num", std::stod(token_name), p1, tNum, nullptr));
  } else {
    auto token_map_it = token_map.find(token_name);
    if (token_map_it == token_map.end()) throw token_name;
    if ((token_name == "+" || token_name == "-") &&
        (tokens->empty() || tokens->back().type == tOpenParanth))
      tokens->push(Token("num", 0, p1, tNum, nullptr));
    tokens->push(token_map_it->second);
  }
}

void Model::check(std::queue<Token>* tokens) {
  const bool checkMatrix[tokenTypes][tokenTypes] = {
      {1, 0, 0, 1, 1},  // tOpenParanth
      {0, 1, 1, 0, 0},  // tClosedParanth
      {1, 0, 0, 1, 0},  // tNum
      {0, 1, 1, 0, 0},  // tBinOperator
      {1, 0, 0, 1, 0},  // tFunc
  };
  std::queue<Token> tmp;
  Token currToken;
  Token prevToken = tokens->front();
  tokens->pop();
  tmp.push(prevToken);
  while (!tokens->empty()) {
    currToken = tokens->front();
    if (!checkMatrix[currToken.type][prevToken.type])
      throw prevToken.name + currToken.name;
    tmp.push(currToken);
    prevToken = currToken;
    tokens->pop();
  }
  if (prevToken.type != tNum && prevToken.type != tClosedParanth)
    throw prevToken.name;
  *tokens = tmp;
}

void Model::convert(std::queue<Token>* tokens, std::queue<Token>* converted) {
  Token token;
  std::stack<Token> support;
  while (!tokens->empty()) {
    token = tokens->front();
    if (token.type == tNum) {
      converted->push(token);
    } else if (token.type >= tBinOperator) {
      while (!support.empty() &&
             (token.priority < support.top().priority ||
              (token.priority == support.top().priority && token.name != "^")))
        moveToken(&support, converted);
      support.push(token);
    } else if (token.type == tOpenParanth) {
      support.push(token);
    } else if (token.type == tClosedParanth) {
      while (support.top().type != tOpenParanth) moveToken(&support, converted);
      support.pop();
    }
    tokens->pop();
  }
  while (!support.empty()) moveToken(&support, converted);
}

void Model::moveToken(std::stack<Token>* src, std::queue<Token>* dest) {
  dest->push(src->top());
  src->pop();
}

double Model::calculate(std::queue<Token>* tokens, double x) {
  std::stack<Token> support;
  Token token;
  Token a, b;
  while (!tokens->empty()) {
    token = tokens->front();
    if (token.name == "x") token.value = x;
    std::visit(
        overloaded{[&](Token::unary_function function) {
                     a = support.top();
                     support.pop();
                     support.push(
                         Token("num", function(a.value), p1, tNum, nullptr));
                   },
                   [&](Token::binary_function function) {
                     a = support.top();
                     support.pop();
                     b = support.top();
                     support.pop();
                     support.push(Token("num", function(b.value, a.value), p1,
                                        tNum, nullptr));
                   },
                   [&](nullptr_t) { support.push(token); }},
        token.function);
    tokens->pop();
  }
  return support.top().value;
}

extern "C" {

  myNamespace::Model* createModel() {
    return new myNamespace::Model();
  }

  void deleteModel(myNamespace::Model* model) {
    delete model;
  }

  int calculateValue(myNamespace::Model* model, const char* expression, double x) {
    std::string expr(expression);
    try {
      model->calculateValue(expr, x);
      return 0;
    } catch (std::string e) {;
      return -1;
    }
  }

  int calculateGraph(myNamespace::Model* model, const char* expression, double xmin, double xmax) {
    std::string expr(expression);
    try {
      model->calculateGraph(expr, xmin, xmax);
      return 0;
    } catch (std::string e) {
      return -1;
    }
  }

  double getResult(myNamespace::Model* model) {
    return model->getResult();
  }

  void getGraphData(myNamespace::Model* model, double* xData, double* yData, size_t* size) {
    auto graphData = model->getGraphData();
    auto& xVector = graphData.first;
    auto& yVector = graphData.second;

    *size = xVector.size();
    std::copy(xVector.begin(), xVector.end(), xData);
    std::copy(yVector.begin(), yVector.end(), yData);
  }
}
