#ifndef MODEL_H
#define MODEL_H

#include <iostream>
#include <map>
#include <queue>
#include <regex>
#include <set>
#include <stack>
#include <vector>

#include "token.h"

namespace myNamespace {

/**
 * @brief The Model class - Part of MVC-pattern. Used to describe business logic
 * of SmartCalc application.
 */
class Model {
 public:
  using graph_type = std::pair<std::vector<double>, std::vector<double>>;

  /**
   * @brief Model - basic constructor of Model object.
   */
  Model();

  /**
   * @brief getResult - public method used to return result of calculation.
   * @return double type value. Result of calculation.
   */
  double getResult() const;

  /**
   * @brief getGraphData - public method used to return result of calculation
   * for function graph plotting.
   * @return std::pair<QVector<double>, QVector<double>> type value. Presents
   * values for plotting.
   */
  graph_type getGraphData() const;

  /**
   * @brief calculateValue - public method used to be called in Controller to
   * send to Model string with expression and to call processing Model methods.
   * @param expression - std::string& type value. Presents string with
   * expression recieved from View via Controller.
   * @param x - double type value. Contains X-var value recieved from View.
   */
  void calculateValue(std::string& expression, double x);

  /**
   * @brief calculateGraph - public method used to be called in Controller to
   * send to Model string with expression and to call processing Model methods
   * to prepare data for graph plotting.
   * @param expression - std::string& type value. Presents string with
   * expression recieved from View via Controller.
   * @param xmin - double type value. Contains lower limit value for X
   * defenition area.
   * @param xmax - double type value. Contains higher limit value for X
   * defenition area.
   */
  void calculateGraph(std::string& expression, double xmin, double xmax);

 private:
  /**
   * @brief result - double type value, Model class property used to contain a
   * result of calculation.
   */
  double result;

  /**
   * @brief graphData - Model class property used to contain a result of
   * calculation for graph Plotting as std::pair<QVector<double>,
   * QVector<double>>.
   */
  graph_type graphData;

  /**
   * @brief process - method used to process expression recieved from View.
   * Consists of parsing, checking, converting to reverse Polish notation parts.
   * @param expression - std::string& type value. Represents expression recieved
   * from View.
   * @return std::queue<Token> type value. Represents Token-objects queue stored
   * in reverse Polish notation.
   */
  std::queue<Token> process(std::string& expression);

  /**
   * @brief parse - method used to process string expression and split it into
   * Token objects.
   * @param expression - std::string& type value. Represents expression recieved
   * from View.
   * @param tokens - std::queue<Token>* type value. Points to the queue where
   * parsed Token objects will be stored.
   */
  void parse(std::string& expression, std::queue<Token>* tokens);

  /**
   * @brief setTokenMap - method used to create all possible Tokens SmartCalc
   * can work with as parsed token name as a key and suitable Token object as
   * value.
   * @return std::map<std::string, Token> type value.
   */
  std::map<std::string, Token> setTokenMap();

  /**
   * @brief readFunction - method to parse functions from string expression.
   * @param expression - std::string& type value. Represents expression recieved
   * from View.
   * @param index - current position in expression starting from which it is
   * necessary to process the string.
   * @return std::string type value. Name of function (mod operator also) that
   * was parsed.
   */
  std::string readFunction(std::string& expression, size_t& index);

  /**
   * @brief readNumber - method to parse numbers from string expression.
   * @param expression - std::string& type value. Represents expression recieved
   * from View.
   * @param index - current position in expression starting from which it is
   * necessary to process the string.
   * @return std::string type value. Number as a string that was parsed.
   */
  std::string readNumber(std::string& expression, size_t& index);

  /**
   * @brief pushToken - method used to check if the parsed token is correct and
   * can be processed by SmartCalc. In case if token is correct suitable Token
   * object will be pushed in Tokens queue.
   * @param token_name - std::string& type value, represents substring that was
   * parsed as a token.
   * @param tokens - std::queue<Token>* value type. Points to queue where parsed
   * tokens will be stored.
   * @param token_map - const std::map<std::string, Token>& value type.
   * Represents all possible Tokens SmartCalc can work with as parsed token name
   * as a key and suitable Token object as value.
   */
  void pushToken(std::string& token_name, std::queue<Token>* tokens,
                 const std::map<std::string, Token>& token_map);

  /**
   * @brief check - method used to check correct logic in user input expression.
   * Checks Tokens for suitability.
   * @param tokens - std::queue<Token>* type value. Points to the queue where
   * tokens are stored after being parsed.
   */
  void check(std::queue<Token>* tokens);

  /**
   * @brief convert - method used to convert queue with parsed tokens to queue
   * where tokens stored in reverse Polish notation.
   * @param tokens - std::queue<Token>* type value. Points to the queue where
   * tokens are stored after being parsed.
   * @param converted - std::queue<Token>* type value. Points to the queue where
   * tokens will be stored after being converted.
   */
  void convert(std::queue<Token>* tokens, std::queue<Token>* converted);

  /**
   * @brief moveToken - method used to move token from one queue to another.
   * @param src - std::stack<Token>* type value. Points the source queue.
   * @param dest - std::stack<Token>* type value. Points the destination queue.
   */
  void moveToken(std::stack<Token>* src, std::queue<Token>* dest);

  /**
   * @brief calculate - method used to calculate the result of expression by
   * processing recieved queue with tokens in reverse Polish notation.
   * @param tokens - std::queue<Token>* type value. Points to the queue where
   * RPN tokens are stored.
   * @param x - double type value. Contains value for X-var.
   * @return double type value. Presents the result of calculation.
   */
  double calculate(std::queue<Token>* tokens, double x);
};

/**
 * @brief The overloaded class - Class to work with std::visit in calculation.
 */
template <class... Ts>
struct overloaded : Ts... {
  using Ts::operator()...;
};

template <class... Ts>
overloaded(Ts...) -> overloaded<Ts...>;

}  // namespace myNamespace

extern "C" {
  myNamespace::Model* createModel();
  void deleteModel(myNamespace::Model* model);
  int calculateValue(myNamespace::Model* model, const char* expression, double x);
  int calculateGraph(myNamespace::Model* model, const char* expression, double xmin, double xmax);
  double getResult(myNamespace::Model* model);
  void getGraphData(myNamespace::Model* model, double* xData, double* yData, size_t* size);
}

#endif  // MODEL_H
