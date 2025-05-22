import numpy as np
import pandas as pd
from sklearn.ensemble import GradientBoostingRegressor
from sklearn.linear_model import RidgeCV
from sklearn.model_selection import train_test_split
from sklearn.pipeline import make_pipeline, make_union
from sklearn.preprocessing import Normalizer
from tpot.builtins import StackingEstimator
from xgboost import XGBRegressor
from tpot.export_utils import set_param_recursive

# NOTE: Make sure that the outcome column is labeled 'target' in the data file
tpot_data = pd.read_csv('PATH/TO/DATA/FILE', sep='COLUMN_SEPARATOR', dtype=np.float64)
features = tpot_data.drop('target', axis=1)
training_features, testing_features, training_target, testing_target = \
            train_test_split(features, tpot_data['target'], random_state=42)

# Average CV score on the training set was: -42.24274755657518
exported_pipeline = make_pipeline(
    Normalizer(norm="l1"),
    StackingEstimator(estimator=RidgeCV()),
    Normalizer(norm="l2"),
    StackingEstimator(estimator=XGBRegressor(learning_rate=0.001, max_depth=7, min_child_weight=17, n_estimators=100, n_jobs=1, objective="reg:squarederror", subsample=0.7000000000000001, verbosity=0)),
    GradientBoostingRegressor(alpha=0.85, learning_rate=0.1, loss="ls", max_depth=10, max_features=0.7000000000000001, min_samples_leaf=6, min_samples_split=13, n_estimators=100, subsample=0.7500000000000001)
)
# Fix random state for all the steps in exported pipeline
set_param_recursive(exported_pipeline.steps, 'random_state', 42)

exported_pipeline.fit(training_features, training_target)
results = exported_pipeline.predict(testing_features)
